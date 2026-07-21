package com.kondra.vm.loader;

import com.kondra.vm.common.loader.VmxFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyVmxFinder implements VmxFinder {
    private final List<File> searchDirs;

    public MyVmxFinder() {
        searchDirs = new ArrayList<>();
    }

    @Override
    public void addSearchDir(File dir) {
        if (dir != null && dir.isDirectory() && !searchDirs.contains(dir)) {
            searchDirs.add(dir);
        }
    }

    @Override
    public File find(String name) {
        File file = new File(name);
        if (file.exists() && file.isFile()) {
            return file;
        }

        for (File dir : searchDirs) {
            File curr = new File(dir, name);
            if (curr.exists() && curr.isFile()) {
                return curr;
            }
        }
        return null;
    }

    public void reset() {
        searchDirs.clear();
    }
}
