# Selfbot
This is a simple Discord selfbot, written in Java using the [JDA](https://github.com/DV8FromTheWorld/JDA) library.

## Features
* `me` command for showing when you are doing an action
* Quote other users' messages
* Cleanup your own messages if you realize you were spamming
* Set your Discord game (Playing ...)
* Use "custom emojis"; when you type :emojiname: your preset text will automatically be edited in
* Make evaluations in Nashorn (javascript) using JDA methods
* Many more features coming soon

## Requirements
* JDK1.8

## Setup

1. **Download the latest release JSelfBot.jar from the [releases page](https://github.com/jagrosh/Selfbot/releases).** You can download the config.txt and/or emojis.json if you wish, although they are just templates and not necessary.

2. **Find your User Token.** In browser or desktop Discord, type `Ctrl-Shift-I`. Go to the Console section, and type `localStorage.token`. Your user token will appear. ⚠ **Do not share this token with anyone** ⚠ This token provides complete access to your Discord account, so never share it!

3. **Create or edit config.txt.** If you didn't download the template, create a new file called `config.txt` in the same directory as JSelfBot.jar. On the first line, paste your user token (_without_ the quotation marks). On the second line, type the prefix you want to use (for example, if you put `self.` on this line, the help command would be `self.help`)
Example config.txt:
```
Mfa.sjaslkkngejfnesjkaNiCETokeNBrosdhsajshfjkjlajf
self.
```

4. **You don't need to do anything with the emojis.json file.** This file gets automatically loaded at start, and saved when you make edits to your emoji list with the `set` and `delete` commands. 

5. **Run JSelfBot.jar.** You'll want to do this via Command Line or Terminal. On most operating systems, you can right click in a directory (sometimes you need to hold shift) and click to open a Command Prompt or Terminal. If your operating system doesn't support this, just open a command line program of some sort and navigate to the selfbot directory (if you don't know how to do this, there are plenty of tutorials online). Once in the directoy, run with `java -jar JSelfBot.jar`

## Suggestions, Bugs, Feature Requests

If you have suggestions for the selfbot, you find bugs, or you'd like to request a feature, please join [my Discord Server](https://discord.gg/0p9LSGoRLu6Pet0k). The goal of this selfbot is having useful features and being easy to use.

## Final Notes

Using selfbots is not officially supported by Discord. If you do something stupid, you _can_ get your account banned completely. Additionally, many users find selfbots annoying or unsafe. Use a selfbot responsibly. Don't use it to spam or to intentionally annoy other users. Any actions you make with a selfbot fall solely on you.
