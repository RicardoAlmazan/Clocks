nohup java -cp target/clocks-server-1.0.jar mx.ipn.escom.clocks.server.App > server.log 2>&1 &
echo $! > ./app.pid
