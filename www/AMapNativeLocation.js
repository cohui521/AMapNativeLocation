var exec = require('cordova/exec');

var AMapNativeLocation = {};

AMapNativeLocation.getNativeLocation = function (success, error) {
    exec(success, error, 'AMapNativeLocation', 'getNativeLocation', []);
};

module.exports = AMapNativeLocation;