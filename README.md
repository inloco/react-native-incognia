![Alt logoImage][logo]

Inloco React Native SDK
===
To integrate the SDK to your app, check our [documentation].

## Latest Updates

Check our [changelog].

[logo]:  https://mobile-api.s3.amazonaws.com/Extras/inloco-logo-medium.png
[documentation]: https://docs.inloco.ai/first-steps/integrating-sdk/react
[changelog]: https://docs.inloco.ai/updates/changelog/react-native

---

### Publishing Plugin Releases

- Preparation
    - Update Master
        - Change the version at `plugin/package.json`, `plugin/package.json`, `plugin/react-native-inlocoengage.podspec` and `plugin/android/build.gradle`
        - Test the plugin new version
            - Link the new plugin locally to update the example app

            ```bash
            cd plugin 
            yarn link

            cd ../example
            yarn link react-native-inlocoengage
            yarn install
            ```
            - Change the sdk version at `example/ios/Podfile` and execute `pod install --repo-update`
            - Run the android and iOS example app
    - Make the PR with the modifications and wait for the approval
    - Create and Push the new version tag with the updated master

    ```bash
    git tag -a x.y.z -m "Release x.y.z"
    git push origin x.y.z
    ```

    - Verify the package-lock.json and package.json if are with the right version.
- Publishing
    - Is likable to make some checks before publishing, so go to the plugin folder and pack, to see what is going installed at npm installation command.

    ```bash
    cd plugin
    npm pack
    ```

    After doing that, **check if the generated pack is Ok and deleted**

    - Login to the npm (you have to be included to the Inloco organization at npm to make the publish, so certificate that you already have the access)

    ```bash
    npm login
    ```

    - Publish the plugin

    ```bash
    npm publish
    ```

    Go to the [https://www.npmjs.com/package/react-native-inlocoengage](https://www.npmjs.com/package/react-native-inlocoengage) to check if the version was updated. You can check by installing at the example project as well (remove the node_module and install with command `yarn install`)

- Update the public repository
    - Go to [https://github.com/In-Loco-Media/react-native-inlocoengage](https://github.com/In-Loco-Media/react-native-inlocoengage) repo to update with the new version of the plugin
        - Update with the last commit of the private repository
        - Push with the commit "Release x.y.z" and push
- Update the Documentation
    - Update the documentation related to the plugin at the docs.inloco and make the request to the owner of the documentation.
