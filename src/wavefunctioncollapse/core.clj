(ns wavefunctioncollapse.core
  (:require [clojure.spec.alpha :as s]
            [clojure.walk :refer [stringify-keys]])
  (:import (com.github.sjcasey21.wavefunctioncollapse
            OverlappingModel
            SimpleTiledModel)
           (java.awt.image BufferedImage)))

(s/check-asserts true)

(s/def ::data #(instance? BufferedImage %))
(s/def ::N int?)
(s/def ::width int?)
(s/def ::height int?)
(s/def ::periodic-input boolean?)
(s/def ::periodic-output boolean?)
(s/def ::symmetry (s/and int? #(<= 0 % 8)))
(s/def ::ground int?)
(s/def ::seed (s/and int? #(<= Integer/MIN_VALUE % Integer/MAX_VALUE)))
(s/def ::limit pos-int?)

(s/fdef overlapping-model
  :args (s/cat :data ::data
               :N ::N
               :width ::width
               :height ::height
               :kwargs (s/keys* :req-un [::periodic-input
                                         ::periodic-output
                                         ::symmetry
                                         ::ground
                                         ::seed
                                         ::limit]))
  :ret #(instance? BufferedImage %))

(defn overlapping-model
  "Generates a new image using the Overlapping Model.

  ## Positional Arguments
  `data` A `BufferedImage` object to use as the initial sample.  
  `N` The size of the patterns.  
  `width` Width of the output generation in pixels.  
  `height` Heght of the output generation in pixels.

  ## Keyword Arguments  
  `periodic-input` Whether or not the input is periodic.  
  `periodic-output` Whether or not the output should be periodic.  
  `symmetry` Allowed symmetries from 1 (no symmetry) to 8 (all mirrored / rotated).  
  `ground` Id of the specific pattern to use as the bottom of the generation.  
  `seed` Seed to use for the random generator.  
  `limit` Maximum number of iterations before finishing."
  [data
   N
   width
   height
   & {:keys
      [periodic-input periodic-output symmetry ground seed limit]
      :or
      {periodic-input true
       periodic-output false
       symmetry 8
       ground 0
       seed (rand-int Integer/MAX_VALUE)
       limit 0}}]
  (let [model (OverlappingModel.
               data
               N
               width
               height
               periodic-input
               periodic-output
               symmetry
               ground)
        result (.run model seed limit)
        graphics (.graphics model)]
    {:seed seed
     :image graphics}))

(s/def ::tilesize int?)

(s/def ::name string?)
(s/def ::symmetry (s/and string? #{"L" "T" "I" "\\" "X"}))
(s/def ::tile (s/keys :req-un [::name ::symmetry]))
(s/def ::tiles (s/coll-of ::tile))

(s/def ::left string?)
(s/def ::right string?)
(s/def ::neighbor (s/keys :req-un [::left ::right]))
(s/def ::neighbors (s/coll-of ::neighbor))

(s/def ::subset (s/or :k keyword? :s string?))
(s/def ::subsets (s/map-of ::subset (s/coll-of string?)))

(s/def ::config (s/keys :req-un [::tilesize ::tiles ::neighbors]
                        :opt-un [::subsets]))

(s/def ::periodic boolean?)
(s/def ::black boolean?)
(s/def ::unique boolean?)

(s/fdef simple-tiled-model
  :args (s/cat :config ::config
               :width ::width
               :height ::height
               :kwargs (s/keys
                        :opt-un
                        [::subset ::periodic ::black ::unique ::seed ::limit]))
  :ret #(instance? BufferedImage %))

(defn simple-tiled-model
  "Generate a new image using the Simple Tiled Model.

  ## Positional Arguments
  `config` Tiles, subset and constraint definitions.  
  `images` Map of `BufferedImage`'s keyed by their `tilename`.  
  `width` Output width in number of tiles.  
  `height` Output height in number of tiles.  

  ## Keyword Arguments
  `subset` Name of the subset defined in `config` to use.  
  `periodic` Whether or not the output should be periodic.  
  `black`  
  `unique`  
  `seed` Seed to use for the random generator.  
  `limit` Maximum number of iterations before finishing."
  [config
   images
   width
   height
   & {:keys [subset periodic black unique seed limit]
      :or {subset nil
           periodic false
           black false
           unique false
           seed (rand-int Integer/MAX_VALUE)
           limit 0}}]
  (s/assert ::config config)
  (let [{:keys [tilesize tiles neighbors subsets]} config
        tiles (map stringify-keys tiles)
        neighbors (map stringify-keys neighbors)
        subsets (reduce-kv (fn [m k v] (assoc m (name k) (into-array v))) {} subsets)
        subset (if subset (name subset))
        model (SimpleTiledModel.
               tilesize
               tiles
               neighbors
               subsets
               images
               subset
               width
               height
               periodic
               black
               unique)
        result (.run model seed limit)
        graphics (.graphics model)]
    {:seed seed
     :image graphics}))
