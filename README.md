# x8l
Make a new markup language named x8l which can replace xml in most areas but always use smaller size.

[![Build Status](https://travis-ci.org/cyanpotion/x8l.svg?branch=master)](https://travis-ci.org/cyanpotion/x8l)
[![Build status](https://ci.appveyor.com/api/projects/status/594i6j3y8w8o2a69?svg=true)](https://ci.appveyor.com/project/XenoAmess/x8l)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

if you use maven you can always go jitpack for it.
https://jitpack.io/#XenoAmess/x8l

```text
<<here goes a basic demo of x8l>
<<first,a comment is like this>  
< <or this this>  
<<<or even this this.< in a comment need not be transcoded.>  
<<use %% to transcode.for example, %>, and this is still in it.>  
<<the content between the first < and the second < is treated as "attributes".>  
<<the order of attributes is important, and node with different order of same attributes are different.>
<<attribute can have = in it.if so, it will be departed into key and value.>
<<key is the part left than the first =,and value is the rest content.>
<a=b>>
<<for example, in the upper node, "a" is a key and "b" is a value>
<<remember, the first =.>
<a=b=c>>
<<that means this node's key is "a" and value is "b=c">
<<if there is no "=" in a attribute then the whole string is the key,and "" is the value>
<<spaces between attributes are treated as nothing, so does '\r' '\n' '\t'>
<<which means you can write it like this>
<views
    windowWidth=1280
    windowHeight=1024
    scale=2.0
    fullScreen=0
>>
<<and it equals to >
<views windowWidth=1280 windowHeight=1024 scale=2.0 fullScreen=0>>
<<the content between the second < and the %> is treated as "children".>
<<children must be nodes, and children's parent is the node which it in>
<<there are 3 kinds of nodes, "content node", "text node", and "comment node">
<<only "content node" have attributes and contents>
so what is a text node? that is a good question.so this is text node.

<be careful!> a space in text node is meaningful and cannot be deleted!>
that means:
<<>
<< >
<<
>
is 3 different nodes.
if you want to delete all text node with "empty char" content,you can use trim.
that is the main content.
you can now run the demo and see the output of the tree of this readme.
that should be helpful/
thanks.
            --XenoAmess
            
and here goes some reallife x8l files:

<commonSettings
    titleText=GamepadMassage
    gameWindowClassName=com.xenoamess.gamepad_massage.FalseGameWindow
    logoClassName=com.xenoamess.gamepad_massage.FalseWorld
    titleClassName=com.xenoamess.cyan_potion.gameWindowComponents.Title
    worldClassName=com.xenoamess.gamepad_massage.FalseWorld
>>
<views
    windowWidth=1280
    windowHeight=1024
    scale=2.0
    fullScreen=0
>>
<specialSettings
    autoShowGameWindowAfterInit=0
    noConsoleThread=1
>>
<debug>>
<keymap using>
    <GLFW_KEY_W>XENOAMESS_KEY_UP>
    <GLFW_KEY_A>XENOAMESS_KEY_LEFT>
    <GLFW_KEY_S>XENOAMESS_KEY_DOWN>
    <GLFW_KEY_D>XENOAMESS_KEY_RIGHT>
    <GLFW_KEY_UP>XENOAMESS_KEY_UP>
    <GLFW_KEY_LEFT>XENOAMESS_KEY_LEFT>
    <GLFW_KEY_DOWN>XENOAMESS_KEY_DOWN>
    <GLFW_KEY_RIGHT>XENOAMESS_KEY_RIGHT>
    <GLFW_KEY_ESCAPE>XENOAMESS_KEY_ESCAPE>
    <GLFW_KEY_ENTER>XENOAMESS_KEY_ENTER>
    <GLFW_KEY_SPACE>XENOAMESS_KEY_SPACE>
    <GLFW_KEY_LEFT_SHIFT>XENOAMESS_KEY_LEFT_SHIFT>
    <GLFW_KEY_RIGHT_SHIFT>XENOAMESS_KEY_RIGHT_SHIFT>
    <GLFW_MOUSE_BUTTON_LEFT>XENOAMESS_MOUSE_BUTTON_LEFT>
    <GLFW_MOUSE_BUTTON_RIGHT>XENOAMESS_MOUSE_BUTTON_RIGHT>
    <GLFW_MOUSE_BUTTON_MIDDLE>XENOAMESS_MOUSE_BUTTON_MIDDLE>
>
<backup worldClassName=com.xenoamess.rpg_module.world.World>>
<backup worldClassName=com.xenoamess.modern_alchemy.scene.ProgramScene>>
<backup worldClassName=com.xenoamess.gamepad_massage.FalseWorld>>
<backup gameWindowClassName=com.xenoamess.cyan_potion.GameWindow>>



<merge version=1.0>
    <0001>
        <en>Thanks!>
        <<just comment>
<ch>谢谢!>
    >
    <<just comment>
    <0002>
        <en>Hello?>
<<just comment>
<ch>您好？>
    >
>

<merge version=1.0>
    <0001>
        <en>Thanks!>
        <<just comment>
<ch>谢谢!>
    >
>
```
