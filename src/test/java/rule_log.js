var fs = require('fs');
var logger = fs.createWriteStream('/Users/master/Documents/workspace/Test-UI-AndroidAuto/log/urlLog.log', {
  flags: 'a' // 'a' means appending (old data will be preserved)
})
function logPageFile(url) {
    logger.write(url + '\r\n');
}
module.exports = {
    summary: function() {
        return "恒大APP请求";
    },
    // replaceResponseStatusCode: function(req, res, code) {
    //         if (req.headers.host == "qm.hdfax.com"||req.headers.host == "qstatic.hdfax.com") {
    //             logPageFile(req.url);
    //     }

    // }
    replaceServerResDataAsync: function(req,res,serverResData,callback){
        if ((req.headers.host).indexOf("hdfax") >0) { 
                logPageFile(req.url);
                logPageFile(serverResData);
        }
                callback(serverResData);

    }

}