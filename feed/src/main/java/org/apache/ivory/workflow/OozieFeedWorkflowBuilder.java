/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ivory.workflow;

import org.apache.hadoop.fs.Path;
import org.apache.ivory.IvoryException;
import org.apache.ivory.converter.AbstractOozieEntityMapper;
import org.apache.ivory.converter.OozieFeedMapper;
import org.apache.ivory.entity.ClusterHelper;
import org.apache.ivory.entity.ExternalId;
import org.apache.ivory.entity.v0.Entity;
import org.apache.ivory.entity.v0.EntityType;
import org.apache.ivory.entity.v0.cluster.Cluster;
import org.apache.ivory.entity.v0.feed.Feed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OozieFeedWorkflowBuilder extends OozieWorkflowBuilder<Feed> {

    @Override
    public Map<String, Object> newWorkflowSchedule(Feed feed)
            throws IvoryException {

        List<Cluster> clusters = new ArrayList<Cluster>();
        List<Path> paths = new ArrayList<Path>();
        for (org.apache.ivory.entity.v0.feed.Cluster feedCluster :
                feed.getClusters().getCluster()) {
            String clusterName = feedCluster.getName();
            Cluster cluster = configStore.get(EntityType.CLUSTER, clusterName);
            Path workflowPath = new Path(ClusterHelper.getLocation(cluster, "staging") +
                    feed.getStagingPath());

            AbstractOozieEntityMapper converter = new OozieFeedMapper(feed);
            Path bundlePath = converter.convert(cluster, workflowPath);
            clusters.add(cluster);
            paths.add(bundlePath);
        }
        return createAppProperties(clusters, paths);
    }

    @Override
    public Cluster[] getScheduledClustersFor(Feed feed)
            throws IvoryException {

        List<Cluster> clusters = new ArrayList<Cluster>();
        for (org.apache.ivory.entity.v0.feed.Cluster feedCluster :
                feed.getClusters().getCluster()) {
            String clusterName = feedCluster.getName();
            Cluster cluster = configStore.get(EntityType.CLUSTER, clusterName);
            clusters.add(cluster);
        }
        return clusters.toArray(new Cluster[clusters.size()]);
    }

    @Override
    public List<ExternalId> getExternalIds(Entity entity, Date start, Date end) throws IvoryException {
        throw new IvoryException("getExternalIds is not supported for Feeds!");
    }
}