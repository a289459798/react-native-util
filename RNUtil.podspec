require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNUtil"
  s.version      = package["version"]
  s.summary      = package['description']
  s.author       = package['author']
  s.homepage     = package['homepage']
  s.license      = package['license']
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "zhangzy@5ichong.cn" }
  s.platform     = :ios, "11.0"
  s.source       = { :git => "https://github.com/author/RNUtil.git", :tag => "master" }
  s.source_files  = "ios/**/*"
  s.requires_arc = true
  s.swift_version    = '5.0'

  s.dependency "React"
  s.dependency 'Qiniu', '~> 7.1'
  s.dependency 'TZImagePickerController', '~> 3.3.1'
  s.dependency "SDWebImage"
  s.dependency 'JXPhotoBrowser'


end

