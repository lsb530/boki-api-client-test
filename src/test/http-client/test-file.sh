curl -f -L -o ijhttp.zip "https://jb.gg/ijhttp/latest"
unzip -nq ijhttp.zip
rm ijhttp.zip
./ijhttp/ijhttp -e local -v http-client.env.json boki.http
rm -r ijhttp