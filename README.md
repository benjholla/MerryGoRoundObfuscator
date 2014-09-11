MerryGoRound Obfuscator
========================

TODO: Actually write some documentation...

Inspired by an earlier [toy example](https://gist.github.com/benjholla/f049c464d09550bd6b35) of laundering information, this is a homebrewed obfuscator that generates code that will produce a program that writes a desired output to an output stream.  It obscures data by encoding a one time use Huffman tree in the program's control flow graph, which is either recursively or iteratively used to decode a given input.  The size of the Huffman tree is configurable by changing the input block size.

While technically this code is working it is still very much beta at this time.
