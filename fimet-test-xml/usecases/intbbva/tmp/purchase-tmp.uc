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
      "name":"Oxxo3",
      "model":"National",
      "parser":"National",
      "address": "127.0.0.1",
      "port": 7251,
      "server":false,
      "adapter":"Exclusive",
      "extension":{
        "validations":{
            "DE39":"Equals(m.getValue(39),\"00\")",
            "NotDE39":"IsTrue(m.hasField(39))",
            "NotNullDE39":"IsNotNull(m.getValue(39))",
            "DE39In00,01":"In(m.getValue(39),\"00\",\"01\")"
        }
      }
    },{
      "name":"Oxxo3Iss",
      "model":"National",
      "parser":"National",
      "address": "127.0.0.1",
      "port": 7251,
      "server":true,
      "adapter":"Exclusive",
      "extension": {
        "timeout": true
      }
    }
  ]
}