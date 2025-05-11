from http.server import HTTPServer, SimpleHTTPRequestHandler
import webbrowser
import os

def run_server(port=8000):
    server_address = ('', port)
    httpd = HTTPServer(server_address, SimpleHTTPRequestHandler)
    print(f"Server running at http://localhost:{port}")
    print("Press Ctrl+C to stop the server")
    
    # Open the browser automatically
    webbrowser.open(f'http://localhost:{port}/car_diagnosis_graph.html')
    
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nShutting down server...")
        httpd.server_close()

if __name__ == '__main__':
    run_server() 