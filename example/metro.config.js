// const path = require('path');
// const blacklist = require('metro-config/src/defaults/blacklist');
// const escape = require('escape-string-regexp');
// const pak = require('../package.json');
// const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');
const path = require('path');
const { getDefaultConfig } = require('@react-native/metro-config');
const { getConfig } = require('react-native-builder-bob/metro-config');
const pkg = require('../package.json');

const root = path.resolve(__dirname, '..');
/**
 * Metro configuration
 * https://reactnative.dev/docs/metro
 *
 * @type {import('metro-config').MetroConfig}
 */
// const config = {};
module.exports = getConfig(getDefaultConfig(__dirname), {
  root,
  pkg,
  project: __dirname,
});

// const modules = Object.keys({
//   ...pak.peerDependencies,
// });

// module.exports = {
//   projectRoot: __dirname,
//   watchFolders: [root],

//   // We need to make sure that only one version is loaded for peerDependencies
//   // So we blacklist them at the root, and alias them to the versions in example's node_modules
//   resolver: {
//     blacklistRE: blacklist(
//       modules.map(
//         (m) =>
//           new RegExp(`^${escape(path.join(root, 'node_modules', m))}\\/.*$`)
//       )
//     ),

//     extraNodeModules: modules.reduce((acc, name) => {
//       acc[name] = path.join(__dirname, 'node_modules', name);
//       return acc;
//     }, {}),
//   },
// };

// module.exports = mergeConfig(getDefaultConfig(__dirname), {
//   projectRoot: __dirname,
//   watchFolders: [root],

//   // We need to make sure that only one version is loaded for peerDependencies
//   // So we blacklist them at the root, and alias them to the versions in example's node_modules
//   resolver: {
//     blacklistRE: blacklist(
//       modules.map(
//         (m) =>
//           new RegExp(`^${escape(path.join(root, 'node_modules', m))}\\/.*$`)
//       )
//     ),

//     extraNodeModules: modules.reduce((acc, name) => {
//       acc[name] = path.join(__dirname, 'node_modules', name);
//       return acc;
//     }, {}),
//   },

//   transformer: {
//     getTransformOptions: async () => ({
//       transform: {
//         experimentalImportSupport: false,
//         inlineRequires: true,
//       },
//     }),
//   },
// });
