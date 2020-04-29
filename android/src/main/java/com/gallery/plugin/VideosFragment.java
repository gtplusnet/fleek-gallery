package com.gallery.plugin;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gallery.plugin.galleryplugin.R;
import com.getcapacitor.JSObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class VideosFragment extends Fragment {

  private Spinner spinner;
  static GridView gallery;
  private ArrayList<String> images;
  private ArrayList<String> selectedVideo;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
    TextView cancel = rootView.findViewById(R.id.cancel);
    TextView next = rootView.findViewById(R.id.next);
    spinner = rootView.findViewById(R.id.spinner);
    cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // cancel return
      }
    });

    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (selectedVideo.size() != 0) {
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;

            File f = savebitmap("thumbnail");
          //return all selected video to app.\
            JSONObject object = new JSONObject();
            JSONArray jsonArray = new JSONArray();
          for (int x = 0; x < selectedVideo.size(); x++) {
              try {
                  jsonArray.put(selectedVideo.get(x));
                  object.put("thumbnail",f.getAbsolutePath());
                  object.put("data",jsonArray);
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
          GalleryPlugin.returnResponse(object);
          getActivity().finish();
        } else {
          Toast.makeText(getContext(), "Nothing is selected.", Toast.LENGTH_SHORT).show();
        }
      }
    });

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

          if ("Camera".equals(spinner.getSelectedItem().toString())) {
              GalleryPlugin.returnResponse("camera_request");
              getActivity().finish();
          } else if (spinner.getSelectedItem().toString().equals("Video")) {
              GalleryPlugin.returnResponse("request_fleektok");
              getActivity().finish();
          }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });



    gallery = rootView.findViewById(R.id.gallery_grid);
    gallery.setAdapter(new ImageAdapterGallery(getActivity()));
    gallery.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
    gallery.setDrawSelectorOnTop(false);
    gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1,
                              int position, long arg3) {

        if (null != images && !images.isEmpty()) {
          Toast.makeText(
            getContext(),
            "position " + position + " " + images.get(position),
            Toast.LENGTH_LONG).show();
        }
      }

    });
    return rootView;
  }
  public void setSelectedIndex(int index, boolean selected) {
    if (selected) {
      selectedVideo.add(images.get(index));
    } else {
      selectedVideo.remove(images.get(index));
    }
  }

    private File savebitmap(String filename) {
        String extStorageDirectory = Environment.getExternalStorageDirectory() + "/Android/data/";
        OutputStream outStream = null;

        File file = new File(extStorageDirectory,filename + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".png");
        }else
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // make a new bitmap from your file
            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(selectedVideo.get(0), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

            outStream = new FileOutputStream(file);
            bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }

  private class ImageAdapterGallery extends BaseAdapter {
    private Activity context;
    private Object flag[];

    public ImageAdapterGallery(Activity localContext) {
      context = localContext;
      images = getAllShownImagesPath(context);
      selectedVideo = new ArrayList<>();
    }

    @Override
    public int getCount() {
      flag = new Object[images.size()];
      return images.size();
    }

    @Override
    public Object getItem(int position) {
      return position;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      final ImageView picturesView = new ImageView(context);
      LinearLayout layout = new LinearLayout(getContext());
      RelativeLayout relativeLayout = new RelativeLayout(getContext());
      if (flag[position] == null) {
        layout.setLayoutParams(new LinearLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);


        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams checkParam = new RelativeLayout.LayoutParams(
          RelativeLayout.LayoutParams.WRAP_CONTENT,
          RelativeLayout.LayoutParams.WRAP_CONTENT);

        final CheckBox checkBox = new CheckBox(getContext());


        checkBox.setEnabled(false);
        checkBox.setVisibility(View.INVISIBLE);
        if (selectedVideo.contains(images.get(position))) {
          checkBox.setChecked(true);
          picturesView.setAlpha(0.3f);
        } else {
          checkBox.setChecked(false);
          picturesView.setAlpha(1f);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (selectedVideo.size() == 1) {
                if (selectedVideo.contains(images.get(position))) {
                  checkBox.setChecked(false);
                  picturesView.setAlpha(1f);
                  setSelectedIndex(position, isChecked);
                } else {
                  Toast.makeText(getContext(), "Single Selection Only.", Toast.LENGTH_SHORT).show();
                }
              }
              else {
                if(getFileSize(images.get(position)) <= 101)
                {
                  if (isChecked)
                    picturesView.setAlpha(0.3f);
                  else
                    picturesView.setAlpha(1f);
                    setSelectedIndex(position, isChecked);
                }
                else
                {
                  checkBox.setChecked(false);
                  Toast.makeText(getContext(),"File size exceeded the maximum size (100mb)",Toast.LENGTH_SHORT).show();
                }
              }
          }
        });

        checkParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        checkParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picturesView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (selectedVideo.size() == 1) {
              if (selectedVideo.contains(images.get(position))) {
                checkBox.setChecked(false);
                picturesView.setAlpha(1f);
              }else {
                Toast.makeText(getContext(), "Single Selection Only.", Toast.LENGTH_SHORT).show();
              }
            }else {
              if(getFileSize(images.get(position)) <= 100)
              {
                if (selectedVideo.contains(images.get(position))) {
                  checkBox.setChecked(false);
                  picturesView.setAlpha(1f);
                } else if (selectedVideo.size() == 0) {
                  checkBox.setChecked(true);
                  picturesView.setAlpha(0.3f);
                } else {
                  checkBox.setChecked(true);
                  picturesView.setAlpha(0.3f);
                }
              }
              else{
                Toast.makeText(getContext(),"File size exceeded the maximum size (100mb)",Toast.LENGTH_SHORT).show();
              }
            }
          }
        });
        picturesView.setLayoutParams(new LinearLayout.LayoutParams(230, 230));
        relativeLayout.addView(picturesView);
        relativeLayout.addView(checkBox, checkParam);


      } else {
        layout = (LinearLayout) convertView;
      }

      layout.addView(relativeLayout);

      Glide.with(context).load(images.get(position))
        .into(picturesView);

      return layout;
    }

    public int getFileSize(String path)
    {
      File file = new File(path);
      return Integer.parseInt(String.valueOf(file.length()/1024/1000));
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
      Uri uri;
      Cursor cursor;
      final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
      int column_index_data, column_index_folder_name;
      ArrayList<String> listOfAllImages = new ArrayList<String>();
      String absolutePathOfImage = "";
      uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

      cursor = activity.getContentResolver().query(uri, projection, null,
        null, orderBy + " DESC");

      column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      column_index_folder_name = cursor
        .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
      while (cursor.moveToNext()) {
        absolutePathOfImage = cursor.getString(column_index_data);

        listOfAllImages.add(absolutePathOfImage);
      }
      return listOfAllImages;
    }
  }
}
