var express = require('express');
var fs = require('fs');
var request = require('request');
var cheerio = require('cheerio');
var htmlparser = require("htmlparser");
var app = express();
var ejs = require('ejs');

app.get('/scrape', function(req, res){

  url = 'http://www.parknshop.com/Fresh-Food/Fruits/c/070000';

    // The structure of our request call
    // The first parameter is our URL
    // The callback function takes 3 parameters, an error, response status code and the html
    var name = [];
    var price = [];

    request(url, function(error, response, html){
        var $ = cheerio.load(html);
        var cnt = 0;
        $('.name').each(function(i, elem){
            //console.log($(this).text()+" ");
            name[i] = $(this).text();
            cnt++;
        });
        $('.display-price').each(function(i, elem){
            price[i] = parseFloat($(this).children().first().next().text().slice(3));
            //console.log(price[i]);
           // json.name = $(this).text();
        });
        
        var item = []
        for(var i=1;i<cnt;i++){
            var json = {name: "", price: ""};
            json.name = name[i];
            json.price = price[i-1];
            //console.log(price[i]);
            item[i-1] = json;
        }
        var json_data = JSON.stringify(item);
        console.log(json_data);

function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    
    var CSV = '';    
    //Set Report title in first row or line
    
    CSV += ReportTitle + '\r\n\n';

    //This condition will generate the Label/Header
    if (ShowLabel) {
        var row = "";
        
        //This loop will extract the label from 1st index of on array
        for (var index in arrData[0]) {
            
            //Now convert each value to string and comma-seprated
            row += index + ',';
        }

        row = row.slice(0, -1);
        
        //append Label row with line break
        CSV += row + '\r\n';
    }
    
    //1st loop is to extract each row
    for (var i = 0; i < arrData.length; i++) {
        var row = "";
        
        //2nd loop will extract each column and convert it in string comma-seprated
        for (var index in arrData[i]) {
            row += '"' + arrData[i][index] + '",';
        }

        row.slice(0, row.length - 1);
        
        //add a line break after each row
        CSV += row + '\r\n';
    }

    if (CSV == '') {        
        alert("Invalid data");
        return;
    }   
    
    //Generate a file name
    var fileName = "MyReport_";
    //this will remove the blank-spaces from the title and replace it with an underscore
    fileName += ReportTitle.replace(/ /g,"_");   
    
    //Initialize file format you want csv or xls
    var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
    console.log(uri);
    res.redirect(uri);
    
}
 JSONToCSVConvertor(json_data, "data set", true);
    });

})

app.listen('8081');

console.log('Magic happens on port 8081');

exports = module.exports = app;