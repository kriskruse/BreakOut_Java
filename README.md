# BreakOut_Java


main -> Setup(), GameLoop(), End()
GameLoop() -> inputHandle(), Update(), Draw() 

inputHandle() - Control and input handling
Update() - Update blocks and game state
Draw() - Draw gamestate to screen.

Gamestate Class -
Ball, Score, Platform, BlockCluster (2D Array of Blocks)

Blockcluster()
Alive = n*m -> updates
Block Class
Hp = 1

Platform()
Size = width, heigth
Position = x,y

Score() [Advanced placeholder]

Ball()
Position =
Vector =
BounceSpeedAdd =

# Collsions
An idea is to have a collision function in the game loop class look through collsions

It would be performant to have the desired collision enabled objects colledted in one list. 

The collisions should be checked using the ball as the moving part.

The collision function should update the ball vector to fit the collision.
With any specific collision rules some parts like the platform has.



# TODO
AH -> Collision
Draw gamestate to screen





