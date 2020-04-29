
  Pod::Spec.new do |s|
    s.name = 'GalleryPlugin'
    s.version = '0.0.1'
    s.summary = 'simple gallery plugin'
    s.license = 'MIT'
    s.homepage = 'https://github.com/francisdurante/gallery-plugin-capacitor'
    s.author = 'francisdurante'
    s.source = { :git => 'https://github.com/francisdurante/gallery-plugin-capacitor', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end