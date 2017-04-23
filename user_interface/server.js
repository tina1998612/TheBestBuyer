var fs = require('fs');
var express = require('express');
var app = express();

fs.writeFile("test.txt", "Hey there!", function(err) {
    if(err) {
        return console.log(err);
    }

    console.log("The file was saved!");
}); 

app.use(express.static('public'));

app.get('/', function(req, res) {
    res.sendFile(__dirname + '/index.html');
});
app.listen(3000, function() {
    console.log('App listening on port 3000!');
});