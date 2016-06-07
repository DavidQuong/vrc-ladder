# vips is required for sharp-loader
brew install vips \
  --with-webp \
  --without-pango \
  --without-python \
  --without-pygobject3 \
  --with-graphicsmagick

# run local dev server
npm i && npm run dev

# run production build
npm run build
