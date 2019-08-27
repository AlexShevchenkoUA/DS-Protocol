# Digital Signature Protocol

<h1> Overview </h1> 

<p>Project contains implementation of digital signature protocol based on ElGamal cryptosystem.</p>

<h2>Componetns</h2>

<p> All functionality are duplicated: use <code>long</code> for maximum performance and <code>BigInteger</code> for variable key leanght and
maximum security.</p>

<p> As cryptographic hash function was built hash function based on Merkle-Damgard scheme with a Misty cipher as the main component. As a digital signature algorithm El-Gamal system that uses modular arithmetic was used. </p>

<p> For application execution was created CLI that gives possibility to sign any file with creating additional metadata. </p>
