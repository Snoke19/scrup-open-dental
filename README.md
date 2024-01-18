Scrapped data from this website https://www.opendental.com/site/apiaccountmodules.html;

Small example:
```json
{
   "item":[
      {
         "name":"API AccountModules",
         "item":[
            {
               "name":"/accountmodules/1337/Aging",
               "request":{
                  "method":"GET",
                  "header":[
                     {
                        "key":"Authorization",
                        "value":"{{token_auth}}",
                        "type":"default"
                     }
                  ],
                  "url":{
                     "raw":"{{url_local}}{{version_api}}/accountmodules/1337/Aging",
                     "host":[
                        "{{url_local}}{{version_api}}"
                     ],
                     "path":[
                        "accountmodules",
                        "1337",
                        "Aging"
                     ]
                  },
                  "description":"AccountModules GET Aging\n<p>Version Added: 22.3.6</p>\n<p>Gets the Aging information for a patient and their family, similarly to how it shows in the <a rel=\"nofollow\">Account Module</a> Aging grid.</p>\n<p><b>PatNum:</b> Required in the URL.</p>\n<p>Returned fields are detailed below:</p><b>Bal_0_30:</b> Aged balance from 0 to 30 days old for the entire family. \n<br><b>Bal_31_60:</b> Aged balance from 31 to 60 days old for the entire family. \n<br><b>Bal_61_90:</b> Aged balance from 61 to 90 days old for the entire family. \n<br><b>BalOver90:</b> Aged balance over 90 days old for the entire family. \n<br><b>Total:</b> Total balance for entire family before insurance estimate. \n<br><b>InsEst:</b> Insurance Estimate for entire family. \n<br><b>EstBal:</b> The total remaining balance for the family after subtracting pending insurance amounts and write-offs. \n<br><b>PatEstBal:</b> The patient balance after subtracting pending insurance amounts and write-offs. \n<br><b>Unearned:</b> The total Unearned / Prepayment balance for the family. \n<br>\n<br>\n<p><b>Example Request:</b><br>\n  GET /accountmodules/1337/Aging<br></p>\n<p><b>Example Response:</b><br><span> {<br>\n   \"Bal_0_30\": 276.28,<br>\n   \"Bal_31_60\": 49.0,<br>\n   \"Bal_61_90\": 64.0,<br>\n   \"BalOver90\": 278.03,<br>\n   \"Total\": 667.31,<br>\n   \"InsEst\": 100.0,<br>\n   \"EstBal\": 567.31,<br>\n   \"PatEstBal\": 24.0,<br>\n   \"Unearned\": 2.46<br>\n   }<br></span></p>\n<p>200 OK<br>\n  400 BadRequest (Patient is deleted, etc)<br>\n  404 NotFound (Patient not found)<br></p>"
               },
               "response":[
                  
               ]
            },
            {
               "name":"/accountmodules/16/PatientBalances",
               "request":{
                  "method":"GET",
                  "header":[
                     {
                        "key":"Authorization",
                        "value":"{{token_auth}}",
                        "type":"default"
                     }
                  ],
                  "url":{
                     "raw":"{{url_local}}{{version_api}}/accountmodules/16/PatientBalances",
                     "host":[
                        "{{url_local}}{{version_api}}"
                     ],
                     "path":[
                        "accountmodules",
                        "16",
                        "PatientBalances"
                     ]
                  },
                  "description":"AccountModules GET PatientBalances\n<p>Version Added: 22.1</p>\n<p>Gets the patient portion for a patient's family, similarly to how it shows in the Account Module's Select Patient grid.</p>\n<p><b>PatNum:</b> Required in the URL.</p>\n<p>Returned fields are detailed below:</p><b>PatNum:</b> The patient's PatNum / family guarantor's PatNum. \n<br><b>Name:</b> The first and last name of the patient, or \"Entire Family\" when regarding the total patient portion of the family. \n<br><b>Balance:</b> The balance of the patient/entire family. \n<br>\n<br>\n<p><b>Example Request:</b><br>\n  GET /accountmodules/16/PatientBalances<br></p>\n<p><b>Example Response:</b><br><span> [<br>\n   {<br>\n   \"PatNum\": 15,<br>\n   \"Name\": \"Smith, John\",<br>\n   \"Balance\": 140.2<br>\n   },<br>\n   {<br>\n   \"PatNum\": 16,<br>\n   \"Name\": \"Smith, Jane\",<br>\n   \"Balance\": 180.55<br>\n   },<br>\n   {<br>\n   \"PatNum\": 17,<br>\n   \"Name\": \"Smith, Junior\",<br>\n   \"Balance\": 68<br>\n   },<br>\n   {<br>\n   \"PatNum\": 18,<br>\n   \"Name\": \"Smith, Sis\",<br>\n   \"Balance\": 0<br>\n   },<br>\n   {<br>\n   \"PatNum\": 15,<br>\n   \"Name\": \"Entire Family\",<br>\n   \"Balance\": 388.75<br>\n   }<br>\n   ]<br></span></p>"
               },
               "response":[
                  
               ]
            },
            {
               "name":"/accountmodules/65/ServiceDateView?isFamily=true",
               "request":{
                  "method":"GET",
                  "header":[
                     {
                        "key":"Authorization",
                        "value":"{{token_auth}}",
                        "type":"default"
                     }
                  ],
                  "url":{
                     "raw":"{{url_local}}{{version_api}}/accountmodules/65/ServiceDateView?isFamily=true",
                     "host":[
                        "{{url_local}}{{version_api}}"
                     ],
                     "path":[
                        "accountmodules",
                        "65",
                        "ServiceDateView"
                     ],
                     "query":[
                        {
                           "key":"isFamily",
                           "value":"true"
                        }
                     ]
                  },
                  "description":"AccountModules GET ServiceDateView\n<p>Version Added: 22.3.32</p>\n<p>Gets a list of all charges and credits to the account for a patient and their family, similarly to how it shows in the <a rel=\"nofollow\">Service Date View</a>.</p>\n<p><b>PatNum:</b> Required in the URL.<br><b>isFamily:</b> Optional. Either \"true\" or \"false\". Return data for the entire patient's family. Default \"false\".</p>\n<p>Returned fields are detailed below:</p><b>ObjectType:</b> The type of object being returned. Either ProcedureLog, Adjustment, PaySplit, ClaimProc, PayPlanCharge, or PayPlan. Otherwise blank. \n<br><b>PrimaryKey:</b> Primary Key corresponding to the ObjectType. Example: procedure.ProcNum=985. \n<br><b>Type:</b> Further details of the ObjectType being displayed: 'Proc', 'Adj-Att.', 'PatPay Att.', 'WriteOff-Att.', 'InsPay-Att.', 'PayPlan Charge Att.', 'PatPay Att. PayPlan', 'Unallocated', 'PatPay', 'WriteOff', 'Adj', 'InsPay', 'PayPlan Credit', 'Dynamic PayPlan Credit', 'PayPlan Charge', 'PatPay PayPlan', 'Day Total', 'Overall Total'. Att. indicates Attached. \n<br><b>ServiceDate:</b> Date service was provided. \n<br><b>TransDate:</b> Date transaction was posted. \n<br><b>Patient:</b> patient.FName. \n<br><b>PatNum:</b> patient.PatNum. \n<br><b>Reference:</b> Description of the of the returned object. \n<br><b>Charge:</b> Amount that was charged. \n<br><b>Credit:</b> Amount that was paid. \n<br><b>Provider:</b> provider.Abbr. \n<br><b>InsBal:</b> Balance of estimated insurance payment. \n<br><b>AcctBal:</b> The remaining patient portion balance. \n<br>\n<br>\n<p><b>Example Request:</b><br>\n  GET /accountmodules/65/ServiceDateView?isFamily=true<br></p>\n<p><b>Example Response:</b><br><span> [<br>\n   {<br>\n   \"ObjectType\": \"Procedure\",<br>\n   \"PrimaryKey\": \"43\",<br>\n   \"Type\": \"Proc\",<br>\n   \"ServiceDate\": \"2021-07-13\",<br>\n   \"TransDate\": \"\",<br>\n   \"Patient\": \"Jaime\",<br>\n   \"PatNum\": \"65\",<br>\n   \"Reference\": \"D2393:#14-MOD-C3(P)\",<br>\n   \"Charge\": \"235.00\",<br>\n   \"Credit\": \"0.00\",<br>\n   \"Provider\": \"DOC2\",<br>\n   \"InsBal\": \"0.00\",<br>\n   \"AcctBal\": \"216.20\"<br>\n   },<br>\n   {<br>\n   \"ObjectType\": \"Adjustment\",<br>\n   \"PrimaryKey\": \"10\",<br>\n   \"Type\": \"Adj-Att.\",<br>\n   \"ServiceDate\": \"\",<br>\n   \"TransDate\": \"2021-07-13\",<br>\n   \"Patient\": \"Jaime\",<br>\n   \"PatNum\": \"65\",<br>\n   \"Reference\": \"Adj- Discount\",<br>\n   \"Charge\": \"0.00\",<br>\n   \"Credit\": \"18.80\",<br>\n   \"Provider\": \"DOC2\",<br>\n   \"InsBal\": \"\",<br>\n   \"AcctBal\": \"\"<br>\n   },<br>\n   {<br>\n   \"ObjectType\": \"\",<br>\n   \"PrimaryKey\": \"\",<br>\n   \"Type\": \"Day Total\",<br>\n   \"ServiceDate\": \"2021-07-13\",<br>\n   \"TransDate\": \"\",<br>\n   \"Patient\": \"\",<br>\n   \"PatNum\": \"\",<br>\n   \"Reference\": \"Total for Date\",<br>\n   \"Charge\": \"235.00\",<br>\n   \"Credit\": \"18.80\",<br>\n   \"Provider\": \"\",<br>\n   \"InsBal\": \"0.00\",<br>\n   \"AcctBal\": \"216.20\"<br>\n   },<br>\n   etc...<br>\n   ]<br></span></p>\n<p>200 OK<br>\n  400 BadRequest (Patient is deleted, etc)<br>\n  404 NotFound (Patient not found)<br></p>"
               },
               "response":[
                  
               ]
            }
         ]
      }
   ]
}
```
