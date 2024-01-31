docker run --rm -i -t -v $PWD:/workdir jetbrains/intellij-http-client \
-e local \
-v http-client.env.json \
-D \
boki.http