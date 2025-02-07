import json


class Request:

    def __init__(self, json_request):
        d = json.dumps(json_request)
        if "opcode" in d.keys() and "inventory_id" in d.keys() and "password" in d.keys() and "paramiters" in d.keys():
            self.request_dic = d
        else:
            raise Exception("Not valid dictionary")

    def opcode(self):
        return self.request_dic["opcode"]

    def inventory_id(self):
        return self.request_dic["inventory_id"]

    def password(self):
        return self.request_dic["password"]

    def parameters(self):
        return self.request_dic["paramiters"]


