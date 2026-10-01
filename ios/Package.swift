// swift-tools-version: 5.9

import PackageDescription

let package = Package(
    name: "ReactNativeIncognia",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "ReactNativeIncognia",
            targets: ["ReactNativeIncognia"]
        ),
    ],
    dependencies: [
        // React Native 0.87 Autolinking SPM paths.
        .package(name: "ReactNative", path: "../../react-native/xcframeworks"),
        .package(name: "React-GeneratedCode", path: "../../react-native/ios"),

        .package(url: "https://github.com/inloco/incognia-spm.git", .upToNextMinor(from: "6.33.2"))
    ],
    targets: [
        .target(
            name: "ReactNativeIncognia",
            dependencies: [
                .product(name: "ReactHeaders", package: "ReactNative"),
                .product(name: "ReactNativeHeaders", package: "ReactNative"),
                .product(name: "ReactNativeDependenciesHeaders", package: "ReactNative"),
                .product(name: "ReactAppHeaders", package: "React-GeneratedCode"),
                .product(name: "Incognia", package: "incognia-spm"),
            ],
            path: ".",
            exclude: ["Incognia.xcodeproj"],
            publicHeadersPath: ".",
            cxxSettings: [
                .headerSearchPath(".")
            ]
        ),
    ],
    cxxLanguageStandard: .cxx20
)