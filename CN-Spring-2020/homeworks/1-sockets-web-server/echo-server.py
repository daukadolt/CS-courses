import socket
import mimetypes

def respond(socket, URI):
    filepath = None
    if URI == '/':
        filepath = 'index.html'
    else:
        filepath = URI[1:]
    fileObj = None
    contentType = None
    try:
        fileObj = open(filepath, 'rb')
        fileObj = fileObj.read()
        contentType = mimetypes.guess_type(filepath)[0]
    except Exception as e:
        print('exception?')
        print(e)
        message = b'HTTP/1.1 404 Not Found\r\nConnection: close\r\nContent-Length: 9\r\nContent-Type: text/plain; charset=utf-8\r\n\r\nNot Found'
        socket.sendall(message)
        return
    # file successfully read
    statusLine = 'HTTP/1.1 200 OK'
    headers = [
            'Content-Length: %i' % len(fileObj),
            'Content-Type: %s' % contentType,
            'Connection: close'
            ]
    message = '%s\r\n' % statusLine
    for header in headers:
        message += header + '\r\n'
    message += '\r\n'
    message = message.encode()
    message += fileObj
    socket.sendall(message)

HOST = '127.0.0.1'
PORT = 65432

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    conn, addr = s.accept()
    with conn:
        print('Connected by', addr)
        while True:
            data = conn.recv(1024)
            if not data:
                break
            decoded = data.decode('utf-8')
            decoded = decoded.split('\r\n')
            method, URI, httpVersion = decoded[0].split()
            if(method == 'GET'):
                respond(conn, URI)
            else:
                break
            conn.sendall(data)
