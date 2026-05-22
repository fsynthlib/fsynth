;(function(config) {
    const HtmlWebpackPlugin = require('html-webpack-plugin');
    config.plugins.push(new HtmlWebpackPlugin({
        templateContent: `
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>fsynth Web demo</title>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="manifest" href="manifest.webmanifest">
    </head>
    <body>
        <div id="root"></div>
    </body>
</html>
`,
        inject: 'body',
        filename: 'index.html'
    }));
})(config);
