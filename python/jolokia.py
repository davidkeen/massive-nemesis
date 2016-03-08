from pyjolokia import Jolokia
import json

JOLOKIA_URL = 'http://localhost:8778/jolokia/'
MBEAN = 'org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=myQueue'

j4p = Jolokia(JOLOKIA_URL)

# Single requests
print(json.dumps(j4p.request(type='read', mbean='java.lang:type=Memory', attribute='HeapMemoryUsage'), indent=4))

# myQueue:QueueSize
# Number of messages in the destination which are yet to be consumed.  Potentially dispatched but unacknowledged.
myQueueSize = j4p.request(type='read', mbean=MBEAN, attribute='QueueSize')
print(json.dumps(myQueueSize, indent=4))
print('myQueue:QueueSize - {0}'.format(myQueueSize["value"]))

# myQueue:AverageEnqueueTime
# Average time a message has been held this destination.
myQueueQueueTime = j4p.request(type='read', mbean=MBEAN, attribute='AverageEnqueueTime')
print('myQueue:AverageEnqueueTime - {0}'.format(myQueueQueueTime["value"]))

# myQueue:InFlightCount
# Number of messages that have been dispatched to, but not acknowledged by, consumers.
myQueueInFlight = j4p.request(type='read', mbean=MBEAN, attribute='InFlightCount')
print('myQueue:InFlightCount - {0}'.format(myQueueInFlight["value"]))

# Bulk requests
j4p.add_request(type='read', mbean=MBEAN, attribute='QueueSize')
j4p.add_request(type='read', mbean=MBEAN, attribute='AverageEnqueueTime')
j4p.add_request(type='read', mbean=MBEAN, attribute='InFlightCount')
bulkData = j4p.getRequests()
print(json.dumps(bulkData, indent=4))

myQueueSize = next((x for x in bulkData if x['request']['attribute'] == 'QueueSize'), None)
print('myQueue:QueueSize - {0}'.format(myQueueSize['value']))

myQueueQueueTime = next((x for x in bulkData if x['request']['attribute'] == 'AverageEnqueueTime'), None)
print('myQueue:AverageEnqueueTime - {0}'.format(myQueueQueueTime['value']))

myQueueInFlight = next((x for x in bulkData if x['request']['attribute'] == 'InFlightCount'), None)
print('myQueue:InFlightCount = {0}'.format(myQueueInFlight['value']))
