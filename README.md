# Kuizu Android Client


## About
This an android client for the kuizu game. The sever can de downloaded at 
[https://github.com/SoraSoftworks/KuizuServer](https://github.com/SoraSoftworks/KuizuServer)
This game is still partially complete, with a lot of features missing such as leaving queue, 
updating profile, etc. 

Checking the server is very important because it provides a specification of the communication
protocol between the client and the server. 
The specification document is here: [http://sora.eliteheberg.fr/uploads/kuizu_spec.pdf](http://sora.eliteheberg.fr/uploads/kuizu_spec.pdf)

## This repo
This repository is an android studio project (created under android studio v1.5). 
Minimum SDK version 15 (Android 4.3.1).

## Test the game
Before testing the game you need to prepare your own server, and modify
```java
    public static String SERVER = "5.135.146.96";
    public static int PORT = 2194;
```
Under the class `Client` to match your server IP address and port.

The default IP address and port are those of my VPS, but I don't keep it running because the game 
still contains few bugs

## Known bugs
 - Entering queue then leaving the game, will produce a ghost player (because it's not removed 
 from the queue).The first player to play with this ghost will have his client crashed. 
 - You can still go back after finding about your opponent, which creates a ghost game, that crashes
 other player's app.
 
## Is this app on Google Play?
No, but soon :)

## How can I donate? 
There will be a paid version on Google Play (probably around 5$) which will not be different from the 
free one. (or will it?)
 
## Contact 
Feel free to contact me at `sora.chouri@gmail.com`.

## License

```
Copyright (c) 2015, Soulaymen Chouri
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by the <organization>.
4. Neither the name of the <organization> nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Soulaymen Chouri ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Soulaymen Chouri BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
```

Peace and love.