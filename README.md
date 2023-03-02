# MiningUtils
 A minecraft mod that adds new blocks to farm ores.

# Blocks
## Vertical Miner
The vertical miner digs a basic 3x3 square in front of him, but can be enlarged to a maximum size of 9x9 with upgrades. It uses energy to work but both the amount of energy used and the working speed can be changed in the config file.
Furthermore, it is also possible to insert an upgrade to make the Vertical Miner work only if it has a redstone signal.

## Block Breaker and Block Placer
The Block Breaker breaks any block in front of it when it receives a redstone signal and needs energy to function.
The drops are placed inside his container and if full they will be dropped on the ground.
The amount of power used can be changed in the config file.
The Block Placer works identically to the Block Breaker except that it places blocks instead of destroying them.
For now the upgrades don't work with these two blocks.

# Searching for gold
## Hog Pan
The Hog Pan needs water to work, obviously it consumes a lot of it but this can be changed in the config file.
Understanding how it works may not be exactly easy to understand at first but it is very simple.
First of all it must have water, and a Hog Pan Mat must be inserted in the slot to the right of the arrow, while earth or mud must be inserted in the left slot.
The block will be consumed and the hog pan will fill by a percentage depending on the block inserted.
With a datapack you can change a tag to make the Hog Pan accept other blocks.

## Bucket Block
The Bucket Block works like a Hopper, you have to right click with a bucket of water in your hand to fill it with water, at this point with the Hog Pan Mat 100% full you can click on it and the water will will get dirty and the Hog Pan Mat will be clean. Always with a bucket in hand if you click on the Bucket Block the bucket fills up with Dirty Water.

## Wave Table
The Wave table needs energy and water to function.
You have to insert the Dirty Water Bucket in the Wave table and you will get an amount of gold and the empty bucket, by default it consumes 500mB of water but you can change it in the config file.
