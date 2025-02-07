import json


class Response:

    def __init__(self, json_request):
        d = json.dumps(json_request)
        if "opcode" in d.keys() and "response" in d.keys():
            self.request_dic = json.dumps(d)
        else:
            raise Exception("Not valid dictionary")

    def opcode(self):
        return self.request_dic["opcode"]

    def response(self):
        return self.request_dic["response"]



