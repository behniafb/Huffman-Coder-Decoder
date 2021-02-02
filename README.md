# Huffman-Coder-Decoder

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#how-to-use">How To Use</a></li>
  </ol>
</details>




## About The Project
This Java application can transform a ```.txt``` file into it's Huffman-coded compressed form(```.cmp``` file), or vise versa.
It is made by me, **Behnia Farahbod**, for my Data Structure's final project. Dedicated to @MRNafisiA , my friend and my Teacher's Assistant

### Built With
* [Java](https://java.com)

## How To Use
When you run the ```Main.java``` class, it asks you for input. You can choose ```1``` for encoding, or ```2``` for decoding.

### For Encoding
The related files are mentioned in this part of the code:
```java
// Encrypt
        if (1 == new Scanner(System.in).nextInt()) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\Huffman-Coded-Text.cmp", false));
            BufferedReader reader = new BufferedReader(new FileReader("src\\Text.txt"));
```
The address of the file that is going to be compressed is given currently ```src\\Text.txt```, that is in ```src``` folder. And also the compressed file will be created in ```src\\Huffman-Coded-Text.cmp```. You can have it wherever you want.

### For Decoding
**WARNING: You are ONLY ALLOWED to decode a compressed file, that was encoded before WITH THIS APP! Unless it should't work; at least without changes in some other lines of the code.**

The related files are mentioned in this part of the code:
```java
 // Decrypt
        else {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\Huffman-Decoded-Text.txt", false));
            BufferedReader reader = new BufferedReader(new FileReader("src\\Huffman-Coded-Text.cmp"));
```
The address of the file that is going to be decoded is given currently ```src\\Huffman-Coded-Text.cmp```, that is in ```src``` folder. And also the decoded file will be ```src\\Huffman-Decoded-Text.txt```, located in ```src``` folder.
