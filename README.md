# wavefunctioncollapse

[![cljdoc badge](https://cljdoc.org/badge/wavefunctioncollapse/wavefunctioncollapse)](https://cljdoc.org/d/wavefunctioncollapse/wavefunctioncollapse/CURRENT)

Bitmap and tilemap generation from a single example with the help of ideas from quantum mechanics. Java port of mxgmn's wavefunctioncollapse library.

Javascript port of mxgmn's [WaveFunctionCollapse](https://github.com/mxgmn/WaveFunctionCollapse "WaveFunctionCollapse") library.

## Usage

This library provides clojure bindings for the java wavefunctioncollapse library found [here](https://github.com/sjcasey21/wavefunctioncollapse "here").

To Install, add `[wavefunctioncollapse "0.1.6"]` to your lein dependencies.

For more information check out the [docs](https://cljdoc.org/d/wavefunctioncollapse/wavefunctioncollapse/CURRENT "here") .

## Quickstart

```clojure
(require [wavefunctioncollapse.core :refer [overlapping-model simple-tiled-model]]
         [mikera.image.core :as img]
	 [cheshire.core :as c])

(overlapping-model 
 (img/load-image "resources/citytemplate.png")
 3
 64
 64
 :periodic-output true)

(def config (-> "resources/knot/data.json" slurp (c/parse-string true)))
(def tilenames (for [tile (:tiles config)] (:name tile)))
(def images (zipmap tilenames 
                    (map #(img/load-image (str "resources/knot/" % ".png")) tilenames)))
(simple-tiled-model
 config
 images
 32
 40
 :subset :fabric)
```

## License

Release under the MIT license. See LICENSE for the full license.
