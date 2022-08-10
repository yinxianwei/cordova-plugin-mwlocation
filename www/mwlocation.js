var exec = require('cordova/exec');

exports.getCurrentPosition = function (success, error, arg0) {
    exec(success, error, 'mwlocation', 'getCurrentPosition', [arg0]);
};
