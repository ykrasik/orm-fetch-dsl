package com.avaje.ebean.fetch;

import com.avaje.ebean.FetchConfig;
import com.avaje.ebean.Query;
import fetch.FetchDescriptorManager;
import fetch.FetchDescriptorManagerImpl;
import fetch.node.FetchDescriptor;
import fetch.node.FetchNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: ykrasik
 * Date: 25/05/14
 */
public class EbeanFetchDescriptorManagerImpl implements EbeanFetchDescriptorManager {
    private final FetchDescriptorManager manager;
    private final int fetchSize;

    public EbeanFetchDescriptorManagerImpl(int fetchSize) {
        this.fetchSize = fetchSize;
        this.manager = new FetchDescriptorManagerImpl();
    }

    @Override
    public FetchDescriptor getFetchDescriptorById(String id) {
        return manager.getFetchDescriptorById(id);
    }

    @Override
    public void scan(String basePathStr) throws IOException {
        manager.scan(basePathStr);
    }

    @Override
    public void loadFile(String path) throws IOException {
        manager.loadFile(path);
    }

    @Override
    public void loadFile(File file) throws IOException {
        manager.loadFile(file);
    }

    @Override
    public void evaluate(String script) throws IOException {
        manager.evaluate(script);
    }

    @Override
    public <T> void apply(Query<T> query, String fetchDescriptorId) {
        final FetchDescriptor fetchDescriptor = manager.getFetchDescriptorById(fetchDescriptorId);
        if (fetchDescriptor == null) {
            // TODO: Throw a better exception
            throw new RuntimeException("Invalid fetchDescriptorId: " + fetchDescriptorId);
        }

        doApply(query, fetchDescriptor, null);
    }

    private <T> void doApply(Query<T> query, FetchNode fetchNode, String fetchPath) {
        final List<FetchNode> children = fetchNode.getChildren();
        if (children.isEmpty()) {
            // Node has no children, parent should have taken care of everything.
            return;
        }

        final String properties = join(children);
        final FetchConfig fetchConfig = new FetchConfig().query(fetchSize);
        query.fetch(fetchPath, properties, fetchConfig);

        // Apply to all children
        for (FetchNode child : children) {
            final String column = child.getColumn();
            final String newFetchPath = fetchPath != null ? fetchPath + '.' + column : column;
            doApply(query, child, newFetchPath);
        }
    }

    private String join(List<FetchNode> fetchNodes) {
        final StringBuilder sb = new StringBuilder();
        for (FetchNode fetchNode : fetchNodes) {
            sb.append(fetchNode.getColumn());
            sb.append(',');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
