package feri.pora.pocket_doctor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import feri.pora.datalib.MeasureData;

public class GdxGraphPlotter extends ApplicationAdapter {
	ShapeRenderer shapeRenderer;
	MeasureData measureData;

	final int WORLD_WIDTH = 300;
	final int WORLD_HEIGHT = 220;

	private OrthographicCamera camera;

	public GdxGraphPlotter (MeasureData measureData) {
		this.measureData = measureData;
	}

	@Override
	public void create () {
		camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
		camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
		camera.update();
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(250/255f, 250/255f, 250/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		if (measureData.getBeatsPerMinute().size() - measureData.getStartIndex() > 1) {
			for (int i = measureData.getStartIndex(); i < measureData.getBeatsPerMinute().size() - 1; i++){
				shapeRenderer.setColor(21/255f, 101/255f, 191/255f, 1);
				shapeRenderer.line(measureData.getTimestamp().get(i) + 2,
						(int)measureData.getBeatsPerMinute().get(i).intValue(),
						measureData.getTimestamp().get(i + 1) + 2,
						(int)measureData.getBeatsPerMinute().get(i + 1).intValue());
				shapeRenderer.setColor(95/255f, 201/255f, 198/255f, 1);
				shapeRenderer.line(measureData.getTimestamp().get(i) + 2,
						measureData.getSpo2().get(i) + 2,
						measureData.getTimestamp().get(i + 1) + 2,
						measureData.getSpo2().get(i + 1) + 2);
			}
		}
		shapeRenderer.end();
	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}
}
