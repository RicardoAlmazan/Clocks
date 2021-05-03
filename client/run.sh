nohup java -cp target/clocks-client-1.0.jar mx.ipn.escom.clocks.client.App > client.log 2>&1 &
echo $! > ./app.pid