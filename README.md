# micro-services

## I) Run service as per below sequence
1)	spring-cloud-config-server
2)	eurekaserver
3)	gatewayserver
4)	Security
5)	Currency-conversion-service
6)	Currency-exchange-service

## II) Register user
http://localhost:8081/auth/register (post)
 
## III) Login to generate token 
http://localhost:8081/auth/login (post)

{
    "username":"gaurav",
    "password":"abc123"
}
 
## IV) Send request 
http://localhost:8091/currency-converter-feign/from/USD/to/INR/quantity/100

### Header
Content-Type   application/json
Authorization  Bearer $2a$10$WUf3BdbOPJFT80dGUxezbeCQUlERxpIr3vwyU396Ci9IMqg91NHvC
 

# For monitor
## 1) Run ealstic search
D:\Ealstic Search\ealsticsearch.bat
http://localhost:9200/

for login user elastcserach credential


## 2) Run kibana
D:\kibana\kibana.bat
http://localhost:5601/

for login user kibana useradmin credential

Step 2: Create a Data View
1.	Open Kibana → Stack Management → Data Views → Create data view.
2.	Enter elktest-* as the index pattern.
3.	Select @timestamp as the time field.
4.	Click Create data view.
________________________________________
Step 3: Create Line Chart – Log Count Over Time
1.	Go to Visualize Library → Create Visualization → Line.
2.	Choose elktest-* as the data view.
3.	Configure X-axis:
o	Aggregation: Date Histogram
o	Field: @timestamp
4.	Configure Y-axis:
o	Aggregation: Count
5.	Optional: set interval (auto or per your time scale).
6.	Save visualization as Log Count Over Time.
________________________________________
Step 4: Create Pie Chart – Distribution of log_level
1.	Go to Visualize Library → Create Visualization → Pie.
2.	Choose elktest-* as the data view.
3.	Buckets → Split Slices:
o	Aggregation: Terms
o	Field: log_level.keyword (use .keyword for exact values)
o	Size: 10 (top 10 log levels)
4.	Y-axis: Count (default)
5.	Save visualization as Log Level Distribution.
________________________________________
Step 5: Create Data Table – Top Log Messages
1.	Go to Visualize Library → Create Visualization → Data Table.
2.	Choose elktest-* as the data view.
3.	Buckets → Split Rows:
o	Aggregation: Terms
o	Field: log_message.keyword
o	Size: 10 (top 10 messages)
4.	Metric: Count (default)
5.	Save visualization as Top Log Messages.
________________________________________
Step 6: Create Dashboard
1.	Go to Dashboard → Create new dashboard.
2.	Click Add from library.
3.	Select the three visualizations you created:
o	Log Count Over Time
o	Log Level Distribution
o	Top Log Messages
4.	Arrange them on the dashboard.
5.	Save dashboard as ELK Test Logs Dashboard.
________________________________________
Step 7: View and Monitor
•	Go to the Discover tab and filter logs as needed.
•	Dashboard will update in real-time as new logs are ingested by Logstash.
•	Use time picker to see specific time ranges.


## 3) Run logstash
D:\logstash\logstash.bat
http://localhost:5601/