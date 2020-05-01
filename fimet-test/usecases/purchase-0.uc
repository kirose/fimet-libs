{
  "message": {
    "parser":"National",
    "adapter": "Exclusive",
    "header": "ISO023400070",
    "mti": "0200",
    "fields": {
    	"2":"9988771122334411",
        "3": "500000",
        "4": "000000010000",
        "7": "0423172306",
        "11": "707361",
        "12": "172306",
        "13": "0423",
        "17": "0423",
        "22": "010",
        "32": "12",
        "37": "001341707000",
        "41": "00040805        ",
        "48": "117812             00000000",
        "49": "484"
	}
  },
  "simulators":[{
      "model":"National",
      "parser":"National",
      "address": "127.0.0.1",
      "port": 4691,
      "server":false,
      "adapter":"Exclusive"
    },{
      "model":"National",
      "parser":"National",
      "address": "127.0.0.1",
      "port": 4691,
      "server":true,
      "adapter":"Exclusive"
    }
  ]
}