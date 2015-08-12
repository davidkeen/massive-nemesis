"""
Server Density Plugin
QueueTime

Version: 1.0.1
"""

try:
    from pyjolokia import Jolokia
except ImportError:
    pass


class QueueTime(object):
    """Plugin class to extract the AverageEnqueueTime data from ActiveMQ.
       Average time a message has been held in a queue.
    """

    def __init__(self, agentConfig, checksLogger, rawConfig):
        self.agentConfig = agentConfig
        self.checksLogger = checksLogger
        self.rawConfig = rawConfig

    def run(self):
        j4p = Jolokia('http://localhost:8161/api/jolokia/')
        j4p.auth(httpusername='username', httppassword='password')

        mbean = 'org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=%s'
        
        # queueOne
        q1 = 'queueOne'
        j4p.add_request(type='read', mbean=(mbean % q1), attribute='AverageEnqueueTime')

        # queueTwo
        q2 = 'queueTwo'
        j4p.add_request(type='read', mbean=(mbean % q2), attribute='AverageEnqueueTime')

        bulk_data = j4p.getRequests()
        queue_one = next((x for x in bulk_data if q1 in x['request']['mbean']), None)
        queue_two = next((x for x in bulk_data if q2 in x['request']['mbean']), None)

        data = {q1: queue_one['value'], q2: queue_two['value']}
        return data
