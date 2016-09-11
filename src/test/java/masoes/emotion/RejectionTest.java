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

public class RejectionTest {
	private Rejection rejection;
	private GeometryFactory geometryFactory;
	private Coordinate[] coordinates;

	@Before
	public void setUp() {
		geometryFactory = new GeometryFactory();
		rejection = new Rejection();
		coordinates = new Coordinate[] {
				new Coordinate(0, 0),
				new Coordinate(0, -0.5),
				new Coordinate(0.5, -0.5),
				new Coordinate(0.5, 0),
				new Coordinate(0, 0)
		};
	}

	@Test
	public void shouldIntersectsWithBoundaryPoints() {
		for (Coordinate coordinate : coordinates) {
			assertTrue(rejection.getGeometry().intersects(geometryFactory.createPoint(coordinate)));
		}
	}

	@Test
	public void shouldNotContainsPoint() {
		assertFalse(rejection.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.7, 0.7))));
		assertFalse(rejection.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.51, 0.51))));
	}

	@Test
	public void shouldContainsInsidePoint() {
		assertTrue(rejection.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.1, -0.1))));
		assertTrue(rejection.getGeometry().intersects(geometryFactory.createPoint(new Coordinate(0.4, -0.4))));
	}

	@Test
	public void shouldReturnCorrectEmotionType() {
		assertThat(rejection.getEmotionType(), Is.is(EmotionType.NEGATIVE_LOW));
	}
}
