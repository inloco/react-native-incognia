require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-incognia"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://github.com/inloco/react-native-incognia.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm}"

  s.dependency "React-Core"
  s.dependency 'IncogniaTrialBR', '~> 6.10.0'
  s.dependency 'IncogniaBR', '~> 6.10.0'
  s.dependency 'IncogniaCoreBR', '~> 6.10.0'
end
