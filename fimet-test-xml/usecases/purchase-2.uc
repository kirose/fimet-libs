{
  "message": {
    "header": "ISO023400070",
    "mti": "0200",
    "fields": {
    	"2":"9988773461796711",
        "3": "500000",
        "4": "000000012300",
        "7": "0423172306",
        "11": "707361",
        "12": "172306",
        "13": "0423",
        "17": "0423",
        "22": "050",
        "32": "12",
        "37": "001341707361",
        "41": "00040805        ",
        "48": "117812             00000000",
        "49": "484"
	}
  },
  "simulators":[{
      "name":"Oxxo2",
      "model":"National",
      "parser":"National",
      "address": "127.0.0.1",
      "port": 7121,
      "server":false,
      "adapter":"Exclusive",
      "extension":{
        "validations":{
            "DE39":"Equals(\"00\",m.getValue(39))"
        }
      }
    },{
      "name":"Oxxo2Iss",
      "model":"National",
      "parser":"National",
      "address": "127.0.0.1",
      "port": 7121,
      "server":true,
      "adapter":"Exclusive",
      "extension":{
    	"del":["17"],
        "add":{
        	"54":"000000001230"
        }
      }
    }
  ]
}