// Normally, this Karma config is generated and maintained by kotlin-frontend-plugin, based on Gradle config.
// However, because of issues with running Javascript unit tests, and no way to customize needed properties through
// Gradle, the config file had to be customized.
// The customizations allow using Chromium instead of PhantomJS. PhantomJS displayed behavior that was hard to debug,
// plus it's no longer maintained, that's why Chromium was chosen.
// If kotlin-frontend-plugin ever supports such fine-grained customizations (see e.g.
// https://github.com/Kotlin/kotlin-frontend-plugin/pull/101/files), it may be possible to remove this config.

var webpackConfig = require(__dirname + "/webpack.config.js");
webpackConfig.mode = 'development';
webpackConfig.resolve.modules = ["js", __dirname + "/js/web.js", "resources/main", "node_modules", __dirname + "/node_modules"];
webpackConfig.context = __dirname + "/js";

process.env.CHROMIUM_BIN = require('puppeteer').executablePath()

module.exports = function (config) {
config.set({
    "basePath": ".",
    "frameworks": [
        "qunit"
    ],
    "reporters": [
        "progress",
        "junit"
    ],
    "files": [
        "classes/kotlin/test/web_test.js"
    ],
    "exclude": [
        "*~",
        "*.swp",
        "*.swo"
    ],
    "port": 9876,
    "runnerPort": 9100,
    "colors": false,
    "autoWatch": true,
    "browsers": [
        "ChromiumHeadless"
    ],
    "captureTimeout": 6000,
    "singleRun": false,
    "preprocessors": {
        "classes/kotlin/test/web_test.js": [
            "webpack"
        ]
    },
    "plugins": [
        "karma-chrome-launcher",
        "karma-junit-reporter",
        "karma-qunit",
        "karma-webpack"
    ],
    "client": {
        "clearContext": false,
        "qunit": {
            "showUI": true,
            "testTimeout": 50000
        }
    },
    "junitReporter": {
        "outputFile": "reports/karma.xml",
        "suite": "karma"
    },
    "webpack": webpackConfig
})
};
