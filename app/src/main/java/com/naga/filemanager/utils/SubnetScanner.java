package com.naga.filemanager.utils;

/**
 * Created by arpitkh996 on 16-01-2016.
 */

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.netbios.NbtAddress;
import jcifs.smb.SmbFile;

public class SubnetScanner extends Thread {

    private static final int RETRY_COUNT = 5;

    private Thread bdThread;
    private final Object mLock;
    private List<ComputerParcelable> mResults;
    private ScanObserver observer;
    private ExecutorService pool;
    private List<Future<ComputerParcelable>> tasks;
    private Context context;

    public interface ScanObserver {
        void computerFound(ComputerParcelable computer);

        void searchFinished();
    }

    class Task implements Callable<ComputerParcelable> {
        String addr;

        public Task(String str) {
            this.addr = str;
        }

        public ComputerParcelable call() {
            try {
                NbtAddress[] allByAddress = NbtAddress.getAllByAddress(this.addr);
                if (allByAddress == null || allByAddress.length <= 0) {
                    return new ComputerParcelable(null, this.addr);
                }
                return new ComputerParcelable(allByAddress[0].getHostName(), this.addr);
            } catch (UnknownHostException e) {
                return new ComputerParcelable(null, this.addr);
            }
        }
    }

    static {
        configure();
    }

    private static void configure() {
        Config.setProperty("jcifs.resolveOrder", "BCAST");
        Config.setProperty("jcifs.smb.client.responseTimeout", "30000");
        Config.setProperty("jcifs.netbios.retryTimeout", "5000");
        Config.setProperty("jcifs.netbios.cachePolicy", "-1");
    }

    public SubnetScanner(Context context) {
        this.context = context;
        mLock = new Object();
        tasks = new ArrayList<>(260);
        pool = Executors.newFixedThreadPool(60);
        mResults = new ArrayList<>();
    }

    public void run() {
        int ipAddress = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .getConnectionInfo().getIpAddress();
        if (ipAddress != 0) {
            tryWithBroadcast();
            String formatIpAddress = Formatter.formatIpAddress(ipAddress);
            String substring = formatIpAddress.substring(0, formatIpAddress.lastIndexOf(46) + 1);
            if (!isInterrupted()) {
                for (ipAddress = 0; ipAddress < 100; ipAddress++) {
                    this.tasks.add(this.pool.submit(new Task(substring + ipAddress)));
                    this.tasks.add(this.pool.submit(new Task(substring + (ipAddress + 100))));
                    if (ipAddress < 56) {
                        this.tasks.add(this.pool.submit(new Task(substring + (ipAddress + 200))));
                    }
                }
                while (!this.tasks.isEmpty()) {
                    int size = this.tasks.size();
                    int i = 0;
                    while (i < size) {
                        if (!isInterrupted()) {
                            try {
                                ComputerParcelable computer = (ComputerParcelable) ((Future) this.tasks.get(i)).get(1, TimeUnit.MILLISECONDS);
                                this.tasks.remove(i);
                                size--;
                                if (computer.name != null) {
                                    onFound(computer);
                                }
                                ipAddress = size;
                            } catch (InterruptedException e) {
                                return;
                            } catch (ExecutionException e2) {
                                ipAddress = size;
                            } catch (TimeoutException e3) {
                                ipAddress = size;
                            }
                            i++;
                            size = ipAddress;
                        } else {
                            return;
                        }
                    }
                }
                try {
                    this.bdThread.join();
                } catch (InterruptedException e4) {
                }
            } else {
                return;
            }
        }
        synchronized (this.mLock) {
            if (this.observer != null) {
                this.observer.searchFinished();
            }
        }
        this.pool.shutdown();
    }

    private void tryWithBroadcast() {
        this.bdThread = new Thread() {
            public void run() {
                for (int i = 0; i < SubnetScanner.RETRY_COUNT; i++) {
                    try {
                        SmbFile smbFile = new SmbFile("smb://");
                        smbFile.setConnectTimeout(5000);
                        SmbFile[] listFiles = smbFile.listFiles();
                        for (SmbFile smbFile2 : listFiles) {
                            SmbFile[] listFiles2 = smbFile2.listFiles();
                            for (SmbFile files : listFiles2) {
                                try {
                                    String substring = files.getName().substring(0, files.getName().length() - 1);
                                    UniAddress byName = UniAddress.getByName(substring);
                                    if (byName != null) {
                                        SubnetScanner.this.onFound(new ComputerParcelable(substring, byName.getHostAddress()));
                                    }
                                } catch (Throwable e) {

                                }
                            }
                        }
                    } catch (Throwable e2) {

                    }
                }
            }
        };
        this.bdThread.start();
    }

    private void onFound(ComputerParcelable computer) {
        mResults.add(computer);
        synchronized (this.mLock) {
            if (this.observer != null) {
                this.observer.computerFound(computer);
            }
        }
    }

    public void setObserver(ScanObserver scanObserver) {
        synchronized (this.mLock) {
            this.observer = scanObserver;
        }
    }

    public void interrupt() {
        super.interrupt();
        try {
            this.pool.shutdownNow();
        } catch (Throwable th) {
            
        }
    }

    public List<ComputerParcelable> getResults() {
        return new ArrayList<>(this.mResults);
    }

}

