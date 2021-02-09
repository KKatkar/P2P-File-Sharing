# P2P-File-Sharing
Java P2P file sharing software similar to BitTorrent

#### Handshake message
The handshake consists of three parts: handshake header, zero bits, and peer ID. The length of the handshake message is 32 bytes. The handshake header is 18-byte string ‘P2PFILESHARINGPROJ’, which is followed by 10-byte zero bits, which is followed by 4-byte peer ID which is the integer representationof the peer ID.

#### Actual messages
After handshaking, each peer can send a stream of actual messages. An actual message consists  of  4-byte message  length  field,  1-byte  message  type  field,  and  a  message payload with variable size.
<br>
The 4-byte message length specifies the message length in bytes. It does not include the length of the message lengthfield itself.The 1-byte message type field specifies the type of the message.


#### Message Types

###### choke, unchoke, interested, not interested
‘choke’, ‘unchoke’, ‘interested’and ‘not interested’messages have no payload.

###### have
‘have’messages have a payload that contains a 4-byte piece index field. 

###### bitfield
‘bitfield’messages is only sent as the first message right after handshaking is done when a connection is established. ‘bitfield’messages have a bitfield as its payload. Each bit in the bitfield payload represents whether the peer has the corresponding piece or not. The first  byte  of  the  bitfield  corresponds  to  piece  indices  0 –7  from  high  bit  to  low  bit, respectively. The next one corresponds to piece indices 8 –15, etc. Spare bits at the end are set to zero. Peers thatdon’t have anything yet may skip a ‘bitfield’message. 

###### request
‘request’messages have a payload which consists of a 4-byte piece index field. Note that ‘request’message payload defined here is different from that of BitTorrent. We don’t divide a piece into smaller subpieces.

###### piece
‘piece’messages have a payload which consists of a 4-byte piece index field and the content of the piece.
