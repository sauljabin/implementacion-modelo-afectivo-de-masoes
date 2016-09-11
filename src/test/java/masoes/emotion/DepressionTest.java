/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.emotion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import masoes.core.EmotionType;

public class DepressionTest {

	private Depression depression;
	private GeometryFactory geometryFactory;
	private Coordinate[] coordinates;

	@Before
	public void setUp() {
		geometryFactory = new GeometryFactory();
		depression = new Depression();
		coordinates = new Coordinate[] {
				new Coordinate(0, -0.5),
				new Coordinate(0, -1),
				new Coordinate(-1, -1),
				new Coordinate(-1, 0),
				new Coordinate(-0.5, 0),
				new Coordinate(-0.5, -0.5),
				new Coordinate(0, -0.5)
		};
	}

	@Test
	public void shouldIntersectsWithBoundaryPoints() {
		for (Coordinate coordinate : coordinates) {
			assertTrue(depression.getGeometry().intersects(geometryFactory.createPoint(coordinate)));
		}
	}

	@Test
	public void shouldContainsInsidePoint() {
		assertTrue(depression.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(-0.7, -0.7))));
		assertTrue(depression.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(-0.51, -0.51))));
	}

	@Test
	public void shouldNotContainsPoint() {
		assertFalse(depression.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(-0.1, -0.1))));
		assertFalse(depression.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(-1.1, -0.1))));
	}

	@Test
	public void shouldReturnCorrectEmotionType() {
		assertThat(depression.getEmotionType(), Is.is(EmotionType.NEGATIVE_HIGH));
	}

}
