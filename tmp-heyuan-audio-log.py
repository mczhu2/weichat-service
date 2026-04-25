def Main():
    xsh.Screen.Synchronous = True
    xsh.Session.Sleep(2500)
    xsh.Screen.Send('tail -n 800 /usr/local/wechat-service/nohup.out | grep -E "MassTask|mass|voice|audio|SendCDNVoiceMsg|CdnUploadVideo|ERROR|Exception|115" -n || tail -n 300 /usr/local/wechat-service/nohup.out')
    xsh.Screen.Send(chr(13))
    xsh.Session.Sleep(6000)
    xsh.Screen.Send('exit')
    xsh.Screen.Send(chr(13))
