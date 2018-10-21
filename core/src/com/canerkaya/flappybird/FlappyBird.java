package com.canerkaya.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;
	Texture gameover;

	int score = 0;
	int scoringTube = 0;

	BitmapFont font;

	Texture[] birds;
	int flapstate = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 2;
	Circle birdCircle;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	Texture topTube;
	Texture bottomTube;
	float gap = 400;

	float maxTubOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] TubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth()/2;
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		startGame();

	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2 - birds[flapstate].getHeight();

		for (int i = 0; i < numberOfTubes; i++){

			TubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getBackBufferHeight());


		if(gameState == 1) {

			if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score++;

				Gdx.app.log("Score: ", String.valueOf(score));

				if(scoringTube < numberOfTubes - 1)
					scoringTube++;
				else
					scoringTube = 0;

			}


			if(Gdx.input.justTouched()){

				velocity = -30;

			}


			for (int i = 0; i < numberOfTubes; i++) {

				if(tubeX[i] < 0 - topTube.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					TubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}else {

					tubeX[i] = tubeX[i] - tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + TubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + TubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + TubeOffset[i],
						topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + TubeOffset[i],
						bottomTube.getWidth(), bottomTube.getHeight());

			}





			if (birdY > 0 ) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}else{
				gameState = 2;
			}

		} else if(gameState == 0){

			if(Gdx.input.justTouched()){

				gameState = 1;

			}
		}
		else if(gameState == 2)
		{
			batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);

			if(Gdx.input.justTouched()){

				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;

			}

		}

		if(flapstate == 0){
			flapstate = 1;
		} else
		{
			flapstate = 0;
		}



		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth(),
				birdY);
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapstate].getWidth()/2, birds[flapstate].getWidth()/4);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + TubeOffset[i],
			//		topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + TubeOffset[i],
			//		bottomTube.getWidth(), bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){
				gameState = 2;
			}


		}


			//shapeRenderer.end();
	}
	
	/*@Override
	public void dispose () {
		batch.dispose();
	}*/
}
