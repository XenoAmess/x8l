# x8l Readme
# fuck off the markdown.
# you shall see this file in raw.
Make a new markup language named x8l which can completely replace xml but always use smaller size.   
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