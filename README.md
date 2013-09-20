# Hashify Editor
 
Hashify Editor turns any `textarea` into a capable [Markdown][1] editor. It's
similar to John Fraser's [wmd][2], but more modular and much lighter weight.
 
![Hashify Editor][3]
 
 
## API
 
### `Hashify.editor`
 
Turns a `textarea` into a Markdown editor.
 
    Hashify.editor(id [, preview [, callback]])
 
#### Parameters
 
##### `id`
 
The `id` of a `textarea`, or a `textarea` node.
 
##### `preview`
 
Boolean which determines whether the "preview" link is included. Defaults to
`true`.
 
##### `callback`
 
Function to be invoked every time Hashify Editor handles an event. Within the
function, `this` refers to the `textarea`. [Hashify][4], for example, uses a
callback to update the URL with each keystroke.
 
#### Example
 
    Hashify.editor(editor, false, function () {
      setLocation(Hashify.encode(this.value));
    });
 
### `Hashify.encode`
 
Returns the Base64-encoded representation of a binary input string.
 
    Hashify.encode(text)
 
### `Hashify.decode`
 
Returns the binary representation of a Base64-encoded input string.
 
    Hashify.decode(text)
 
 
## Sites using Hashify Editor
 
  - [hashify.me][4]
  - [davidchambersdesign.com][5]
 
 
[1]: http://daringfireball.net/projects/markdown/syntax
[2]: http://code.google.com/p/wmd/
[3]: http://cdn.bitbucket.org/davidchambers/hashify-editor/downloads/hashify-editor.png
[4]: http://hashify.me/
[5]: http://davidchambersdesign.com/