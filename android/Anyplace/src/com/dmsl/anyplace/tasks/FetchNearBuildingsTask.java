/*
* AnyPlace: A free and open Indoor Navigation Service with superb accuracy!
*
* Anyplace is a first-of-a-kind indoor information service offering GPS-less
* localization, navigation and search inside buildings using ordinary smartphones.
*
* Author(s): Timotheos Constambeys
* 
* Supervisor: Demetrios Zeinalipour-Yazti
*
* URL: http://anyplace.cs.ucy.ac.cy
* Contact: anyplace@cs.ucy.ac.cy
*
* Copyright (c) 2015, Data Management Systems Lab (DMSL), University of Cyprus.
* All rights reserved.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of
* this software and associated documentation files (the �Software�), to deal in the
* Software without restriction, including without limitation the rights to use, copy,
* modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject to the
* following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT WARRANTY OF ANY KIND, EXPRESS
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*/

package com.dmsl.anyplace.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dmsl.anyplace.nav.BuildingModel;
import com.dmsl.anyplace.utils.GeoPoint;

public class FetchNearBuildingsTask {
	public List<BuildingModelDistance> distances;
	public List<BuildingModel> buildings;

	public FetchNearBuildingsTask() {

	}

	public void run(List<BuildingModel> buildings, String lat, String lon, int max) {
		double dlat = Double.parseDouble(lat);
		double dlon = Double.parseDouble(lon);
		run(buildings, dlat, dlon, max);
	}

	public void run(List<BuildingModel> loadBuildings, double lat, double lon, int max) {
		this.distances = new ArrayList<BuildingModelDistance>(loadBuildings.size());
		this.buildings = new ArrayList<BuildingModel>(loadBuildings.size());

		for (BuildingModel bm : loadBuildings) {
			BuildingModelDistance bmd = new BuildingModelDistance(bm, lat, lon);

			if (bmd.distance < max) {
				distances.add(bmd);
			}
		}

		Collections.sort(distances);

		for (BuildingModelDistance bmd : distances) {
			buildings.add(bmd.bm);
		}
	}

	public static class BuildingModelDistance implements Comparable<BuildingModelDistance> {
		public BuildingModel bm;
		public Double distance;

		BuildingModelDistance(BuildingModel bm, double lat, double lon) {
			this.bm = bm;
			distance = GeoPoint.getDistanceBetweenPoints(bm.longitude, bm.latitude, lon, lat, "");
		}

		@Override
		public int compareTo(BuildingModelDistance arg0) {
			return this.distance.compareTo(arg0.distance);
		}

	}
}