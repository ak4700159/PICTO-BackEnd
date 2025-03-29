import torch


# GPU 사양 확인 
print("CUDA available:", torch.cuda.is_available())
print("CUDA device name:", torch.cuda.get_device_name(0))
print("Compute Capability:", torch.cuda.get_device_capability(0))

