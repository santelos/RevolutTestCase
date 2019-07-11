To build the project you need to execute "mvn clean install" in the root of the projects<br />

"RevolutTestCase-server.zip" archive will appear in the "target" folder<br />
You have to copy this archive into the folder where you'll be deploy your server<br />
After that you need to unzip it and start the server via /bin/server.sh<br />
There are two args that you can pass into the script (start, stop) to start and stop server.<br />
Properties are in "config" folder <br />

P.S
I'm not sure about "stop" argument, because i wrote it on Windows but use the unix command "kill"