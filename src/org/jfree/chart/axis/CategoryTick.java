package org.jfree.chart.axis;

import org.jfree.ui.TextAnchor;

public abstract class CategoryTick extends Tick {

	protected Comparable category;

	protected CategoryTick(Comparable category, TextAnchor textAnchor,
			TextAnchor rotationAnchor, double angle) {
		super("", textAnchor, rotationAnchor, angle);
		
		this.category = category;
	}

	public Comparable getCategory() {
		return category;
	}
}
