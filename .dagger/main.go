package main

import "dagger/typewriter/internal/dagger"

type Typewriter struct{}

func lockedCache(name string) *dagger.CacheVolume {
	return dag.CacheVolume(
		name,
		dagger.CacheVolumeOpts{
			Sharing: dagger.CacheSharingModeLocked,
		},
	)
}
